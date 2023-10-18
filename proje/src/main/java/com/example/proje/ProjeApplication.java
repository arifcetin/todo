package com.example.proje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjeApplication {

	public static void main(String[] args) {
		 SpringApplication.run(ProjeApplication.class, args);
		String a = "bora";
		String b = "bora";
		String c = "bo"+"ra";
		String d = "ra";
		String e = "bo"+d;
		b = b.concat("a");


		System.out.println(a == b);
		System.out.println(a.equals(b));
		System.out.println(a == c);
		System.out.println(e == a);
		System.out.println(a == "bora");
	}

}
