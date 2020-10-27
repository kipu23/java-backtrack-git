package com.kipu23.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Backtrack {

    private static final Logger logger = LoggerFactory.getLogger(Backtrack.class);

    public static void main(String[] args) {

        try {
            Enumeration<URL> resources = Backtrack.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                Attributes attr = manifest.getMainAttributes();
                String shaNumber = attr.getValue("Git-SHA");
                logger.info("Git SHA number: {}", shaNumber);
            }
        } catch (IOException E) {
            logger.error("Manifest file not found!");
        }

    }

}
