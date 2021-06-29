package com.axel.gatewaypoc.utils;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class GenerateDefaultPairKeysTest {
	
	@Test
	public void generateTest() {
		String aes = UUID.randomUUID().toString();
		String iv = UUID.randomUUID().toString();
		
		 System.out.println("AES " + aes);
         System.out.println("IV " + iv);
		
	}

}
