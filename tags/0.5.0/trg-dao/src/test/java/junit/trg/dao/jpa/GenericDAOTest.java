/* Copyright 2009 The Revere Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package junit.trg.dao.jpa;

import java.util.List;

import test.trg.BaseTest;
import test.trg.dao.jpa.dao.PersonDAO;
import test.trg.dao.jpa.dao.ProjectDAO;
import test.trg.model.Person;
import test.trg.model.Project;

import com.trg.search.ExampleOptions;
import com.trg.search.Search;
import com.trg.search.Sort;

public class GenericDAOTest extends BaseTest {

	private PersonDAO personDAO;
	
	private ProjectDAO projectDAO;

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}
	
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}


	/**
	 * Just quickly check that all the methods basically work. The underlying
	 * implementation is more thoroughly tested elsewhere
	 */
	public void testDAO() {
		Person fred = setup(new Person("Fred", "Smith", 35));
		Person bob = setup(new Person("Bob", "Jones", 58));
		Person cyndi = setup(new Person("Cyndi", "Loo", 58));
		Person marty = setup(new Person("Marty", "McFly", 58));

		
		personDAO.persist(fred);
		personDAO.save(bob);
		fred.setFather(bob);

		assertEquals(bob, personDAO.find(bob.getId()));
		assertEquals(fred, personDAO.find(fred.getId()));

		//count
		assertEquals(2, personDAO.count(new Search()));
		assertEquals(2, personDAO.count(new Search(Person.class)));
		
		//searchAndCount
		assertListEqual(new Person[] { bob, fred }, personDAO
				.searchAndCount(new Search()).getResult());
		assertListEqual(new Person[] { bob, fred }, personDAO
				.searchAndCount(new Search(Person.class)).getResult());

		//searchUnique
		Search s = new Search();
		s.addFilterEqual("id", bob.getId());
		assertEquals(bob, personDAO.searchUnique(s));
		s = new Search(Person.class);
		s.addFilterEqual("father.id", bob.getId());
		assertEquals(fred, personDAO.searchUnique(s));
		
		//searchGeneric
		s = new Search();
		s.addFilterEqual("id", bob.getId());
		s.setResultMode(Search.RESULT_SINGLE);
		s.addField("firstName");
		assertEquals(bob.getFirstName(), personDAO.searchGeneric(s).get(0));
		s.setSearchClass(Person.class);
		assertEquals(bob.getFirstName(), personDAO.searchGeneric(s).get(0));

		//searchUniqueGeneric
		assertEquals(bob.getFirstName(), personDAO.searchUniqueGeneric(s));
		s.setSearchClass(null);
		assertEquals(bob.getFirstName(), personDAO.searchUniqueGeneric(s));
		
		//example
		Person example = new Person();
		example.setFirstName("Bob");
		example.setLastName("Jones");
		
		s = new Search(Person.class);
		s.addFilter(personDAO.getFilterFromExample(example));
		assertEquals(bob, personDAO.searchUnique(s));
		
		example.setAge(0);
		s.clear();
		s.addFilter(personDAO.getFilterFromExample(example));
		assertEquals(null, personDAO.searchUnique(s));
		
		s.clear();
		s.addFilter(personDAO.getFilterFromExample(example, new ExampleOptions().setExcludeZeros(true)));
		assertEquals(bob, personDAO.searchUnique(s));
		
		
		//check searching with null
		assertListEqual(personDAO.search(null), fred, bob);
		assertListEqual(personDAO.searchGeneric(null), fred, bob);
		assertListEqual(personDAO.search(null), fred, bob);
		assertListEqual(personDAO.searchGeneric(null), fred, bob);
		assertEquals(2, personDAO.count(null));
		assertListEqual(personDAO.searchAndCount(null).getResult(), fred, bob);
		assertEquals(2, personDAO.searchAndCount(null).getTotalCount());
		try {
			personDAO.searchUnique(null);
			fail("Should have thrown NullPointerException.");
		} catch (NullPointerException ex) {}
		try {
			personDAO.searchUniqueGeneric(null);
			fail("Should have thrown NullPointerException.");
		} catch (NullPointerException ex) {}
		

		assertTrue(personDAO.removeById(fred.getId()));
		assertEquals(null, personDAO.find(fred.getId()));
		
		assertTrue(personDAO.remove(bob));
		assertEquals(null, personDAO.find(bob.getId()));

		assertEquals(0, personDAO.count(new Search(Person.class)));

		personDAO.flush();
		
		bob.setId(null);
		fred.setId(null);

		personDAO.save(bob);
		personDAO.save(fred);
		
		personDAO.flush();
		personDAO.refresh(fred);
		
		for (Person p : personDAO.save(cyndi, marty)) {
			assertNotNull(p);
		}
		for (Person p : personDAO.find(cyndi.getId(), bob.getId(), fred.getId())) {
			assertNotNull(p);
		}
		
		personDAO.removeByIds(cyndi.getId(), marty.getId());
		personDAO.remove(cyndi, fred);
		for (Person p : personDAO.find(cyndi.getId(), marty.getId(), fred.getId())) {
			assertNull(p);
		}
		
		flush();
		clear();
		
		fred.setId(null);
		personDAO.persist(fred);
		
		flush();
		clear();
		
		Person bob2 = copy(bob);
		bob2.setFirstName("Bobby");
		bob2 = personDAO.save(bob2);
		Person fred2 = copy(fred);
		fred2.setFirstName("Freddie");
		fred2 = personDAO.merge(fred2);

		personDAO.flush();
		
		assertEquals("Bobby", personDAO.find(bob.getId()).getFirstName());
		assertEquals("Freddie", personDAO.find(fred.getId()).getFirstName());
		
		assertTrue(personDAO.isAttached(bob2));
		assertFalse(personDAO.isAttached(bob));
		
		Person[] pp = personDAO.merge(bob, fred);
		assertTrue(pp[0] == bob2);
		assertTrue(pp[1] == fred2);
		
		assertEquals("Bob", personDAO.find(bob.getId()).getFirstName());
		assertEquals("Fred", personDAO.find(fred.getId()).getFirstName());
		
		personDAO.persist(new Person("Andy", "Warhol"), new Person("Jimmy", "Hendrix"));
		
		assertEquals(2, personDAO.count(new Search().addFilterIn("firstName", "Andy", "Jimmy")));
		
		Person a = personDAO.getReference(bob2.getId());
		Person b = personDAO.getReference(bob2.getId() + 10);
		
		pp = personDAO.getReferences(bob2.getId(), bob2.getId() + 10);
		
		assertEquals("Bob", a.getFirstName());
		assertEquals("Bob", pp[0].getFirstName());
		
		try {
			b.getFirstName();
			fail("Entity does not exist, should throw error.");
		} catch (Exception ex) { }
		try {
			pp[1].getFirstName();
			fail("Entity does not exist, should throw error.");
		} catch (Exception ex) { }
	}
	
	/**
	 * Test an example of adding and overriding DAO methods.
	 */
	public void testExtendingDAO() {
		initDB();
		
		//two added methods...
		
		List<Project> expected = projectDAO.search(new Search().addFilterIn("name", "First", "Second"));
		assertListEqual(projectDAO.findProjectsForMember(joeA), expected.toArray());
		
		assertListEqual(projectDAO.search(projectDAO.getProjectsForMemberSearch(joeA).addField("name")), "First", "Second");
		
		//overridden search method to deal with "duration"...
		
		Search s = new Search();
		s.addFilterGreaterThan("duration", 50);
		s.addSort(Sort.asc("duration"));
		List<Project> results = projectDAO.search(s);
		assertTrue(results.size() == 2);
		assertEquals("Second", results.get(0).getName());
		assertEquals("First", results.get(1).getName());
		
		s.clear();
		s.addFilterLessThan("duration", 100);
		s.addSort(Sort.desc("duration"));
		results = projectDAO.search(s);
		assertTrue(results.size() == 2);
		assertEquals("Second", results.get(0).getName());
		assertEquals("Third", results.get(1).getName());
	}

}
