/*This class will maintain single object for all the declared classes*/
package com.triton.aquadrops.util;


public class Singleton {
	private static final Utils utils = new Utils();

	public Singleton() {
		// TODO Auto-generated constructor stub
	}


	public static Utils getUtils() {

		return utils;
	}

}
