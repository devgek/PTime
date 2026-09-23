package com.gek.and.project4.service;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.gek.and.project4.dao.BookingDao;
import com.gek.and.project4.dao.DaoMaster;
import com.gek.and.project4.dao.DaoSession;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.types.PeriodType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
/import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * These tests use the real wall clock (like the production code, which calls
 * {@code Calendar.getInstance()} internally and has no injectable clock), so all fixture
 * dates are placed with generous margins around period boundaries (day/week/month/year) to
 * keep the tests deterministic regardless of when they run.
 */
@RunWith(RobolectricTestRunner.class)
public class BookingServiceTest {

    private DaoSession daoSession;
    private BookingDao bookingDao;
    private BookingService bookingService;
    private long projectId;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        // name=null gives greenDAO/SQLiteOpenHelper an in-memory database, fresh per test.
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, null, null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        bookingDao = daoSession.getBookingDao();
        bookingService = new BookingService(daoSession);

        Project project = new Project();
        project.setTitle("Test Project");
        project.setCompany("Test Company");
        project.setColor("#ff0000");
        project.setActive(true);
        project.setDefaultNote("default note");
        project.setBillable(true);
        projectId = daoSession.getProjectDao().insert(project);
    }

    private long insertBooking(Date from, Date to) {
        Booking booking = new Booking();
        booking.setProjectId(projectId);
        booking.setFrom(from);
        booking.setTo(to);
        if (to != null) {
            booking.setMinutes((int) ((to.getTime() - from.getTime()) / 1000 / 60));
        }
        booking.setBreakHours(0);
        booking.setBreakMinutes(0);
        booking.setBillable(true);
        return bookingDao.insert(booking);
    }

    private long insertBooking(long projectIdOverride, Date from, Date to) {
        Booking booking = new Booking();
        booking.setProjectId(projectIdOverride);
        booking.setFrom(from);
        booking.setTo(to);
        booking.setBreakHours(0);
        booking.setBreakMinutes(0);
        booking.setBillable(true);
        return bookingDao.insert(booking);
    }

    private static Date now() {
        return new Date();
    }

    private static Date minutesAgo(int minutes) {
        return new Date(System.currentTimeMillis() - minutes * 60_000L);
    }

    /**
     * Like {@link #minutesAgo}, but pre-floored to a whole minute (seconds/millis zeroed) the
     * same way {@code DateUtil.getSmoothed} floors "now" when a booking is stopped. Needed so an
     * elapsed-minutes assertion is exact instead of off-by-one depending on which millisecond of
     * the current minute the test happens to run in.
     */
    private static Date minutesAgoFloored(int minutes) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.MINUTE, -minutes);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private static Date daysAgo(int days) {
        return new Date(System.currentTimeMillis() - days * 24 * 60 * 60_000L);
    }

    private static Date atZone(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Calendar calendarOf(LocalDateTime dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(atZone(dateTime));
        return cal;
    }

    private static boolean containsId(List<Booking> bookings, long id) {
        for (Booking booking : bookings) {
            if (booking.getId() != null && booking.getId() == id) {
                return true;
            }
        }
        return false;
    }

    // --- period boundary tests -------------------------------------------------

    @Test
    public void getToday_includesRecentBooking_excludesFarPastBooking() {
        long recentId = insertBooking(minutesAgo(10), now());
        long farPastId = insertBooking(daysAgo(400), daysAgo(400));

        List<Booking> today = bookingService.getToday();

        assertTrue(containsId(today, recentId));
        assertFalse(containsId(today, farPastId));
    }

    @Test
    public void getThisWeek_includesNow_excludesEightDaysAgo() {
        long thisWeekId = insertBooking(minutesAgo(5), now());
        long lastWeekId = insertBooking(daysAgo(8), daysAgo(8));

        List<Booking> week = bookingService.getThisWeek();

        assertTrue(containsId(week, thisWeekId));
        assertFalse(containsId(week, lastWeekId));
    }

    @Test
    public void getThisMonth_includesNow_excludesThirtyTwoDaysAgo() {
        long thisMonthId = insertBooking(minutesAgo(5), now());
        long lastMonthId = insertBooking(daysAgo(32), daysAgo(32));

        List<Booking> month = bookingService.getThisMonth();

        assertTrue(containsId(month, thisMonthId));
        assertFalse(containsId(month, lastMonthId));
    }

    @Test
    public void getThisYear_includesNow_excludesFourHundredDaysAgo() {
        long thisYearId = insertBooking(minutesAgo(5), now());
        // 400 days is always more than one full calendar year, so this is always in a strictly
        // earlier year than "now", regardless of leap years.
        long lastYearId = insertBooking(daysAgo(400), daysAgo(400));

        List<Booking> year = bookingService.getThisYear();

        assertTrue(containsId(year, thisYearId));
        assertFalse(containsId(year, lastYearId));
    }

    @Test
    public void getPriorMonth_returnsOnlyBookingsFromThePreviousCalendarMonth() {
        YearMonth priorMonth = YearMonth.now().minusMonths(1);
        Date midPriorMonth = atZone(priorMonth.atDay(15).atTime(10, 0));
        long priorMonthId = insertBooking(midPriorMonth, midPriorMonth);
        long currentMonthId = insertBooking(minutesAgo(5), now());

        List<Booking> priorMonthBookings = bookingService.getPriorMonth();

        assertTrue(containsId(priorMonthBookings, priorMonthId));
        assertFalse(containsId(priorMonthBookings, currentMonthId));
    }

    @Test
    public void getPriorYear_returnsOnlyBookingsFromThePreviousCalendarYear() {
        int priorYear = LocalDate.now().getYear() - 1;
        // July 15th is far from both the Dec 31 / Jan 1 boundary and Feb 29 edge cases.
        Date midPriorYear = atZone(LocalDate.of(priorYear, 7, 15).atTime(10, 0));
        long priorYearId = insertBooking(midPriorYear, midPriorYear);
        long currentYearId = insertBooking(minutesAgo(5), now());

        List<Booking> priorYearBookings = bookingService.getPriorYear();

        assertTrue(containsId(priorYearBookings, priorYearId));
        assertFalse(containsId(priorYearBookings, currentYearId));
    }

    @Test
    public void getFiltered_withProjectId_returnsOnlyThatProjectsBookings() {
        long otherProjectId = daoSession.getProjectDao().insert(makeProject("Other Project"));
        long ownBookingId = insertBooking(projectId, minutesAgo(5), now());
        insertBooking(otherProjectId, minutesAgo(5), now());

        List<Booking> filtered = bookingService.getFiltered(PeriodType.TODAY, projectId);

        assertEquals(1, filtered.size());
        assertTrue(containsId(filtered, ownBookingId));
    }

    private Project makeProject(String title) {
        Project project = new Project();
        project.setTitle(title);
        project.setCompany("Test Company");
        project.setColor("#00ff00");
        project.setActive(true);
        project.setBillable(true);
        return project;
    }

    // --- start/stop timer tests --------------------------------------------------

    @Test
    public void bookStart_createsRunningBookingUsingProjectDefaults() {
        Booking started = bookingService.bookStart(projectId);

        assertNotNull(started);
        assertNotNull(started.getId());
        assertEquals(Long.valueOf(projectId), started.getProjectId());
        assertNull(started.getTo());
        assertEquals("default note", started.getNote());
        assertEquals(Boolean.TRUE, started.getBillable());
        assertEquals(Integer.valueOf(0), started.getMinutes());
        assertNotNull(started.getFrom());

        // persisted, not just held in memory
        Booking reloaded = bookingDao.load(started.getId());
        assertNotNull(reloaded);
        assertNull(reloaded.getTo());
    }

    @Test
    public void bookStop_sameDay_setsToAndComputesMinutesWithoutSplitting() {
        Booking open = new Booking();
        open.setProjectId(projectId);
        // bookStop() floors "to" to the start of the current minute (DateUtil.getSmoothed), so
        // "from" must be minute-aligned too, otherwise the elapsed minutes would be off by one
        // depending on which millisecond of the current minute the test runs in.
        open.setFrom(minutesAgoFloored(45));
        open.setBreakHours(0);
        open.setBreakMinutes(0);
        open.setBillable(true);
        bookingDao.insert(open);

        Booking stopped = bookingService.bookStop(open);

        assertNotNull(stopped.getTo());
        assertEquals(45, stopped.getMinutes().intValue());
        assertEquals(1, bookingDao.loadAll().size());
    }

    @Test
    public void bookStop_acrossMidnight_splitsIntoOneBookingPerDay() {
        LocalDateTime yesterdayEvening = LocalDate.now().minusDays(1).atTime(20, 0);
        Booking open = new Booking();
        open.setProjectId(projectId);
        open.setFrom(atZone(yesterdayEvening));
        open.setBreakHours(0);
        open.setBreakMinutes(0);
        open.setBillable(true);
        open.setMinutes(0);
        bookingDao.insert(open);

        Booking stopped = bookingService.bookStop(open);

        List<Booking> all = bookingDao.loadAll();
        assertEquals(2, all.size());

        LocalDateTime endOfYesterday = yesterdayEvening.toLocalDate().atTime(23, 59, 59);
        assertEquals(atZone(endOfYesterday), open.getTo());

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        assertEquals(atZone(startOfToday), stopped.getFrom());
        assertNotNull(stopped.getTo());
        assertNotNull(stopped.getId());
        assertTrue(!stopped.getId().equals(open.getId()));
    }

    @Test
    public void bookStop_acrossMidnight_alsoWorksWhenBookingWasNeverPersisted() {
        // Regression test: splitStopBooking() used to call bookingDao.update() directly on the
        // yesterday-part of the split, which throws if the booking has no id yet. It now goes
        // through updateBooking(), which inserts when the id is null.
        LocalDateTime yesterdayEvening = LocalDate.now().minusDays(1).atTime(20, 0);
        Booking open = new Booking();
        open.setProjectId(projectId);
        open.setFrom(atZone(yesterdayEvening));
        open.setBreakHours(0);
        open.setBreakMinutes(0);
        open.setBillable(true);
        // deliberately not inserted: open.getId() is null

        Booking stopped = bookingService.bookStop(open);

        assertNotNull(open.getId());
        assertNotNull(stopped.getId());
        assertEquals(2, bookingDao.loadAll().size());
    }

    @Test
    public void splitStopBooking_multiDayGapWithinSameYear_createsOneBookingPerDay() {
        // Regression test for the middle-day loop, which was never exercised by the
        // midnight-crossing tests above (those only ever have a 1-day gap, so the loop body
        // never runs). Calls splitStopBooking() directly with fixed Calendars instead of going
        // through bookStop(), since bookStop() always derives "stop" from the real wall clock.
        Calendar cStart = calendarOf(LocalDateTime.of(2026, 1, 10, 20, 0));
        Calendar cStop = calendarOf(LocalDateTime.of(2026, 1, 13, 8, 0));

        Booking open = new Booking();
        open.setProjectId(projectId);
        open.setFrom(cStart.getTime());
        open.setBreakHours(0);
        open.setBreakMinutes(0);
        open.setBillable(true);
        open.setMinutes(0);
        bookingDao.insert(open);

        Booking stopped = bookingService.splitStopBooking(open, cStart, cStop);

        List<Booking> all = bookingDao.loadAll();
        assertEquals(4, all.size()); // Jan 10 tail, Jan 11 + Jan 12 middle days, Jan 13 head

        assertEquals(atZone(LocalDateTime.of(2026, 1, 10, 23, 59, 59)), open.getTo());
        assertTrue(hasFullDayBooking(all, LocalDate.of(2026, 1, 11)));
        assertTrue(hasFullDayBooking(all, LocalDate.of(2026, 1, 12)));
        assertEquals(atZone(LocalDateTime.of(2026, 1, 13, 0, 0)), stopped.getFrom());
        assertEquals(cStop.getTime(), stopped.getTo());
    }

    @Test
    public void splitStopBooking_acrossYearBoundary_createsOneBookingPerDayIncludingMiddleDays() {
        // Regression test: the middle-day loop and the day Calendars it built were derived from
        // Calendar.getInstance() ("now"'s year) with only DAY_OF_YEAR overwritten, so a gap that
        // crossed a year boundary silently dropped the days in between (e.g. Dec 31 here).
        Calendar cStart = calendarOf(LocalDateTime.of(2025, 12, 30, 20, 0));
        Calendar cStop = calendarOf(LocalDateTime.of(2026, 1, 2, 8, 0));

        Booking open = new Booking();
        open.setProjectId(projectId);
        open.setFrom(cStart.getTime());
        open.setBreakHours(0);
        open.setBreakMinutes(0);
        open.setBillable(true);
        open.setMinutes(0);
        bookingDao.insert(open);

        Booking stopped = bookingService.splitStopBooking(open, cStart, cStop);

        List<Booking> all = bookingDao.loadAll();
        assertEquals(4, all.size()); // Dec 30 tail, Dec 31 + Jan 1 middle days, Jan 2 head

        assertEquals(atZone(LocalDateTime.of(2025, 12, 30, 23, 59, 59)), open.getTo());
        assertTrue(hasFullDayBooking(all, LocalDate.of(2025, 12, 31)));
        assertTrue(hasFullDayBooking(all, LocalDate.of(2026, 1, 1)));
        assertEquals(atZone(LocalDateTime.of(2026, 1, 2, 0, 0)), stopped.getFrom());
        assertEquals(cStop.getTime(), stopped.getTo());
    }

    private static boolean hasFullDayBooking(List<Booking> bookings, LocalDate day) {
        Date dayBegin = atZone(day.atStartOfDay());
        Date dayEnd = atZone(day.atTime(23, 59, 59));
        for (Booking booking : bookings) {
            if (dayBegin.equals(booking.getFrom()) && dayEnd.equals(booking.getTo())) {
                return true;
            }
        }
        return false;
    }
}
