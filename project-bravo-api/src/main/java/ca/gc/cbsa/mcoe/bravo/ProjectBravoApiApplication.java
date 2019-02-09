package ca.gc.cbsa.mcoe.bravo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ca.gc.cbsa.mcoe.bravo.domain.commercial.HourlyStatsCommercial;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.HourlyStatsCommercialRepository;

@SpringBootApplication
public class ProjectBravoApiApplication implements CommandLineRunner {

	@Autowired
	private HourlyStatsCommercialRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ProjectBravoApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Stats found with findAll():");
		System.out.println("-------------------------------");
		for (HourlyStatsCommercial stats : repository.findHourlyStatsBetween("2019-01-22 02:00:00", "2019-01-22 08:00:00")) {
			System.out.println(stats);
		}
		System.out.println();

	}

}
