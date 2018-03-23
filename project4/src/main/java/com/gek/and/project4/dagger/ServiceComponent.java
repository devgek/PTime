package com.gek.and.project4.dagger;

import com.gek.and.project4.async.ExportImporter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by moo on 23.03.18.
 */
@Singleton
@Component(modules = ServiceModule.class)
public interface ServiceComponent {
	void inject(ExportImporter exportImporter);
}
