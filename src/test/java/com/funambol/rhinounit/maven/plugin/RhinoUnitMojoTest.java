/*
 * Copyright Stefano Fornari 2009
 *
 * This file is part of RhinoUnit.
 *
 * RhinoUnit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.funambol.rhinounit.maven.plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import javax.script.ScriptEngine;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;


/**
 *
 * @author ste
 */
public class RhinoUnitMojoTest extends AbstractMojoTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private RhinoUnitMojo getRhinoUnitMojo(String pomFileName) throws Exception {
        File testPom = new File(getBasedir(), pomFileName);
        RhinoUnitMojo mojo = (RhinoUnitMojo) lookupMojo ("test", testPom );

        MavenProjectStub project = new MavenProjectStub();
        project.setGroupId("funambol");
        project.setArtifactId("rhinounit-maven-plugin-test");
        project.setScriptSourceRoots(
            Arrays.asList(new String[] {"target/test-classes/scripts"})
        );
        project.setTestCompileSourceRoots(
            Arrays.asList(new String[] {"target/test-classes/js"})
        );

        try {
            Field f = RhinoUnitMojo.class.getDeclaredField("project");

            f.setAccessible(true);

            f.set(mojo, project);
        } catch (NoSuchFieldException e) {
            // if there is not the project property, never mind...
        }

        return mojo;
    }

    public void testConfiguration() throws Exception {
        RhinoUnitMojo mojo = getRhinoUnitMojo("target/test-classes/unit/basic-test/full-config.xml");

        mojo.execute();

        ScriptEngine engine = mojo.getEngine("js");
        assertNotNull(engine);
        assertNotNull(engine.get("JSUNIT_VERSION"));
        assertEquals("target/test-classes/scripts", ((List)engine.get("testValue")).get(0));
    }

    public void testRunTestCase() throws Exception {
        RhinoUnitMojo mojo = getRhinoUnitMojo("target/test-classes/unit/basic-test/full-config.xml");

        mojo.execute();

        ScriptEngine engine = mojo.getEngine("js");
        assertNotNull(engine);

        assertEquals("testsuite1.js", engine.eval("runner.getLatestExecutedSuite();"));
    }


}
