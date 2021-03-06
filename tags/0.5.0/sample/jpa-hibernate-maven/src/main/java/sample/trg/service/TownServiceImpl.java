package sample.trg.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sample.trg.dao.TownDAO;
import sample.trg.model.Town;

import com.trg.search.Search;

@Repository
@Transactional
public class TownServiceImpl implements TownService {

	TownDAO dao;
	
	@Autowired
	public void setDao(TownDAO dao) {
		this.dao = dao;
	}
	
	public void persist(Town town) {
		dao.persist(town);
	}
	
	public List<Town> findAll() {
		return dao.findAll();
	}
	
	public Town findByName(String name) {
		return dao.searchUnique(new Search().addFilterEqual("name", name).addFetch("citizens"));
	}
}
