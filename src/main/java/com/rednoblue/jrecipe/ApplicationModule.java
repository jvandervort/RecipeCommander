package com.rednoblue.jrecipe;

import com.google.inject.AbstractModule;

public class ApplicationModule extends AbstractModule {
	@Override
	protected void configure() {
		//bind service to implementation class if you are using interfaces
		//bind(IBook.class).to(Book.class);
	}
}