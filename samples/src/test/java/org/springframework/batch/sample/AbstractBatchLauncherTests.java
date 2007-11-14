/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.sample;

import org.springframework.batch.core.configuration.JobConfiguration;
import org.springframework.batch.execution.launch.JobLauncher;
import org.springframework.batch.execution.runtime.DefaultJobIdentifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Abstract unit test for running functional tests by getting context locations
 * for both the container and configuration separately and having them auto
 * wired in by type. This allows the two to be completely separated, and remove
 * any 'configuration coupling' between the two. However, it is still purely
 * theoretical until a decision is made as to how job configuration and
 * container configuration files are pulled together.
 * 
 * @author Lucas Ward
 * 
 */
public abstract class AbstractBatchLauncherTests extends
		AbstractDependencyInjectionSpringContextTests {

	private static final String CONTAINER_DEFINITION_LOCATION = "simple-container-definition.xml";

	JobLauncher launcher;
	private JobConfiguration jobConfiguration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.test.AbstractSingleSpringContextTests#createApplicationContext(java.lang.String[])
	 */
	protected ConfigurableApplicationContext createApplicationContext(
			String[] locations) {
		ApplicationContext parent = new ClassPathXmlApplicationContext(
				CONTAINER_DEFINITION_LOCATION);
		return new ClassPathXmlApplicationContext(locations, parent);
	}

	public void setLauncher(JobLauncher bootstrap) {
		this.launcher = bootstrap;
	}

	/**
	 * Public setter for the {@link JobConfiguration} property.
	 * 
	 * @param jobConfiguration
	 *            the jobConfiguration to set
	 */
	public void setJobConfiguration(JobConfiguration jobConfiguration) {
		this.jobConfiguration = jobConfiguration;
	}

	protected String getJobName() {
		return jobConfiguration.getName();
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void testLaunchJob() throws Exception {
		// Make sure the job is unique by the test case that runs it, not just its name:
		launcher.run(new DefaultJobIdentifier(getJobName(), this.getClass().getName()));
		launcher.stop();
	}
}
