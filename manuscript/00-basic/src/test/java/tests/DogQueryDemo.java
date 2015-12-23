package tests;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import model.Dog;
import model.QDog;

public class DogQueryDemo {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("sqldemo");
		try {
			EntityManager em = emf.createEntityManager();

// JPA (JPQL)
			List<Dog> dogs = em.createQuery(
				"select d from Dog d where d.name like :name", Dog.class)
				.setParameter("name", "Re%").getResultList();
			System.out.println("dogs = " + dogs);

// Querydsl 4
			List<Dog> dogs2 = new JPAQueryFactory(em)
				.select(QDog.dog)
				.from(QDog.dog)
				.where(QDog.dog.name.startsWith("Re"))
				.fetch();
			System.out.println("dogs2 = " + dogs2);
		} finally {
			emf.close();
		}
	}
}
