package com.hhit.ciapp.services;

import com.hhit.ciapp.classes.Test;
import com.hhit.ciapp.models.TestModel;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class ReflectionsService {
    public List<TestModel> getAllExceptionalTests() {

        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.hhit.ciapp.tests"))));

        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        //List<TestModel> to add them all
        List<TestModel> allExceptionalTests = new ArrayList<>();

        for (Class testClass : allClasses) {
            try {
                Test object = (Test) testClass.newInstance();
                TestModel test = new TestModel();
                test.setTestObject(object);
                test.setPriorityOrder(object.getPriorityOrder());
                test.setTestName(testClass.getSimpleName());
                allExceptionalTests.add(test);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return allExceptionalTests;
    }
}
