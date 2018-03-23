package com.gek.and.project4.dagger;

import com.gek.and.project4.service.BookingImportService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by moo on 23.03.18.
 */
@Module
public class ServiceModule {
	@Provides
	@Singleton
	public static BookingImportService provideBookingImportService() {
		return new BookingImportService();
	}
}
