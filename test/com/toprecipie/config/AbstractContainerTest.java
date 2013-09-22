package com.toprecipie.config;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.toprecipe.configs.AppConfig;

@ContextConfiguration(classes = { AppConfig.class, TestDataConfig.class })
public abstract class AbstractContainerTest extends
		AbstractTransactionalJUnit4SpringContextTests {

}
