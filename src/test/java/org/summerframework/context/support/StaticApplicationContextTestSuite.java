/*
 * Copyright (c) 2011-2025 PiChen
 */

package org.summerframework.context.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.summerframework.beans.MutablePropertyValues;
import org.summerframework.beans.TestBean;
import org.summerframework.beans.factory.LBIInit;
import org.summerframework.context.AbstractApplicationContextTests;
import org.summerframework.context.ApplicationContext;
import org.summerframework.context.ACATest;
import org.summerframework.context.BeanThatListens;

/**
 * Classname doesn't match XXXXTestSuite pattern, so as to avoid
 * being invoked by Ant JUnit run, as it's abstract
 *
 * @author Rod Johnson
 * @version $Revision$
 */
public class StaticApplicationContextTestSuite extends AbstractApplicationContextTests {

    protected StaticApplicationContext sac;

    public StaticApplicationContextTestSuite() throws Exception {
    }

    /**
     * Run for each test
     */
    protected ApplicationContext createContext() throws Exception {
        StaticApplicationContext parent = new StaticApplicationContext();
        parent.addListener(parentListener);
        Map m = new HashMap();
        m.put("name", "Roderick");
        parent.registerPrototype("rod", TestBean.class, new MutablePropertyValues(m));
        m.put("name", "Albert");
        parent.registerPrototype("father", TestBean.class, new MutablePropertyValues(m));
        parent.rebuild();

        StaticMessageSource parentMessageSource = (StaticMessageSource) parent.getBean("messageSource");
        parentMessageSource.addMessage("code1", Locale.getDefault(), "message1");

        this.sac = new StaticApplicationContext(parent);
        sac.addListener(listener);
        sac.registerSingleton("beanThatListens", BeanThatListens.class, new MutablePropertyValues());
        sac.registerSingleton("aca", ACATest.class, new MutablePropertyValues());
        sac.registerPrototype("aca-prototype", ACATest.class, new MutablePropertyValues());
        LBIInit.createTestBeans(sac.defaultBeanFactory);
        sac.rebuild();

        StaticMessageSource sacMessageSource = (StaticMessageSource) sac.getBean("messageSource");
        sacMessageSource.addMessage("code2", Locale.getDefault(), "message2");

        return sac;
    }

    /**
     * Overridden
     */
    public void testCount() throws Exception {
        assertCount(16);
    }

    protected void tearDown() {
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        //	junit.swingui.TestRunner.main(new String[] {PrototypeFactoryTests.class.getName() } );
    }

    public static Test suite() {
        return new TestSuite(StaticApplicationContextTestSuite.class);
    }


}