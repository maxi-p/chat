package com.encryptedmessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public interface Configurations{
	String VIEWS_PATH_RESOLVER = "/WEB-INF/views/";
	
	String RESOURCE_BUNDLE_BASE_NAME = "app";
}
