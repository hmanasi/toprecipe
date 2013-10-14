package com.toprecipe;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.toprecipe.controllers.ControllerTestSuite;
import com.toprecipe.repository.RepositoryTestSuite;
import com.toprecipe.services.ServiceTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ RepositoryTestSuite.class, ServiceTestSuite.class,
		ControllerTestSuite.class })
public class AllTestSuite {

}
