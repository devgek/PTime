package com.gek.and.project4.service;

import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.util.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.inject.Inject;

/**
 * Created by moo on 11.03.18.
 */

public class BookingImportService {
	@Inject
	public BookingImportService() {
	}

	public List<Booking> getBookingList(List<String> importedLines, boolean hasHeader, String ignorePattern, List<ProjectCard> allProjects) {
		List<Booking> importedBookings = new ArrayList<>();
		String[] ignoreStrings = new String[]{};
		if (ignorePattern != null) {
			ignoreStrings = ignorePattern.split(":::");
		}
		List<String> ignoreLines = Arrays.asList(ignoreStrings);

		int l = 0;
		for (String line : importedLines) {
			line = line.trim();

			l++;
			if (l == 1 && hasHeader) {
				continue;
			}

			if (line.length() == 0) {
				continue;
			}

			if (ignoreLine(line, ignoreLines)) {
				continue;
			}

			String[] fields = getFields(line);
			Optional<Booking> oBooking = mapBooking(fields, allProjects);
			if (oBooking.isPresent()) {
				importedBookings.add(oBooking.get());
			}
		}

		return importedBookings;
	}

	private String[] getFields(String line) {
		String[] fields = line.split(";");
		String[] fieldsContent = new String[fields.length];

		for (int i = 0; i < fields.length; i++) {
			String[] content = fields[i].split("\"");
			fieldsContent[i] = content[1];
		}

		return fieldsContent;
	}

	private boolean ignoreLine(String line, List<String> ignoreLines) {
		for (String ignorePattern : ignoreLines) {
			if (line.startsWith(ignorePattern)) {
				return true;
			}
		}

		return false;
	}

	private Optional<Booking> mapBooking(String[] fieldValues, List<ProjectCard> allProjects) {
		if (fieldValues == null || fieldValues.length < 8) {
			return Optional.empty();
		}

		Booking booking = new Booking();
		try {
			Optional<Long> oProjectId = mapProject(fieldValues[0], fieldValues[1], allProjects);
			checkPresent(oProjectId);
			booking.setProjectId(oProjectId.get());

			Optional<Date> oFrom = DateUtil.parseDateTime(fieldValues[2] + " " + fieldValues[3] + ":00");
			checkPresent(oFrom);
			booking.setFrom(oFrom.get());

			Optional<Date> oTo = DateUtil.parseDateTime(fieldValues[2] + " " + fieldValues[4] + ":00");
			checkPresent(oTo);
			booking.setTo(oTo.get());

			Optional<Integer> oHours = DateUtil.parseHM(fieldValues[5], 0);
			checkPresent(oHours);
			booking.setBreakHours(oHours.get());

			Optional<Integer> oMins = DateUtil.parseHM(fieldValues[5], 1);
			checkPresent(oMins);
			booking.setBreakMinutes(oMins.get());

			booking.setNote(fieldValues[7]);

			return Optional.of(booking);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private Optional<Long> mapProject(String customer, String projectTitle, List<ProjectCard> allProjects) {
//		Long projectId = allProjects.
//				stream().
//				map(ProjectCard::getProject).
//				filter(project -> project.getCompany().equals(customer) && project.getTitle().equals(projectTitle)).
//				map(Project::getId).
//				findFirst().
//				orElse(null);

		Long projectId = null;
		for (ProjectCard projectCard : allProjects) {
			if (projectCard.getProject().getCompany().equals(customer) && projectCard.getProject().getTitle().equals(projectTitle)) {
				projectId = projectCard.getProject().getId();
				break;
			}
		}
		return Optional.ofNullable(projectId);
	}

	private void checkPresent(Optional<?> optional) {
		if (!optional.isPresent()) {
			throw new IllegalArgumentException();
		}
	}
}
