package com.farhan.staradmin;

import com.farhan.staradmin.crypto.symetrique.CryptoPaddingLister;
import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.service.AlgorithmeService;
import com.farhan.staradmin.service.ChiffrementUtil;
import com.farhan.staradmin.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EntityScan("com.farhan.staradmin.entity") // Specify the package where your entities are located
@EnableJpaRepositories("com.farhan.staradmin.repository") // Specify the package where your repositories are located
public class SpringBootThymeleafStarAdminApplication implements CommandLineRunner {

	@Autowired
	private AlgorithmeService algorithmeService;
	@Autowired
	private KeyService keyService;
	@Autowired
	private ChiffrementUtil chiffrementUtil;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootThymeleafStarAdminApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<String> names = Arrays.asList("AES", "DES", "RSA", "DSA", "Diffie-Hellman");

		for (String name : names) {
			Algorithme algorithme = new Algorithme(name);
			algorithmeService.saveAlgorithme(algorithme);
		}
		Key key = new Key();
		key.setType("symetrique");
		key.setSize(128);
		key.setName("AES");
		keyService.saveKey(key);
		chiffrementUtil.getKeysByAttribut("symetrique","AES",128).forEach(key1 -> {
			System.out.println(key1.getName());
		});
	}
}
