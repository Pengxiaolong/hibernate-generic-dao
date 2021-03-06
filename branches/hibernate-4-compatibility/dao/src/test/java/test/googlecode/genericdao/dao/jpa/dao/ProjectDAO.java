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
package test.googlecode.genericdao.dao.jpa.dao;

import java.util.List;

import test.googlecode.genericdao.model.Person;
import test.googlecode.genericdao.model.Project;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.googlecode.genericdao.search.Search;

public interface ProjectDAO extends GenericDAO<Project, Long> {
	/**
	 * Returns a list of all projects of which the given person is a member.
	 */
	public List<Project> findProjectsForMember(Person member);
	
	/**
	 * Returns a search that will find all the projects for which the given
	 * person is a member.
	 */
	public Search getProjectsForMemberSearch(Person member);
}
