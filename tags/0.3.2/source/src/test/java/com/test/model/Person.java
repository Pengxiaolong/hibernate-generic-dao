package com.test.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@ManyToOne
	@JoinColumn(name="mother_id")
	private Person mother;
	@ManyToOne
	@JoinColumn(name="father_id")
	private Person father;
	private Integer age;
	private Date dob;
	private Double weight;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="home_id")
	private Home home;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Person getMother() {
		return mother;
	}

	public void setMother(Person mother) {
		this.mother = mother;
	}

	public Person getFather() {
		return father;
	}

	public void setFather(Person father) {
		this.father = father;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Person))
			return false;
		Person p = (Person) o;

		if (!(firstName == null ? p.firstName == null : firstName
				.equals(p.firstName)))
			return false;
		if (!(lastName == null ? p.lastName == null : lastName
				.equals(p.lastName)))
			return false;

		return true;
	}

	public String toString() {
		return "Person::id:" + id + ",firstName:" + firstName + ",lastName:" + lastName + ",age:" + age + ",dob:" + dob;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + (firstName == null ? 0 : firstName.hashCode());
		hash = hash * 31 + (lastName == null ? 0 : lastName.hashCode());
		return hash;
	}
	
	
}
