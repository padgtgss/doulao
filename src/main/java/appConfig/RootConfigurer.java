package appConfig;

import common.util.db.BaseDao;
import common.util.db.BaseDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import common.util.db.DetaDiv;
import common.util.db.MonDB;



//boot:4
//boot:5
@ComponentScan(basePackages = "com.yslt.doulao ")
public class RootConfigurer {

	public void db() {
		new DetaDiv();
	}

	@Bean
	public MonDB mdb() {
		return new MonDB();
	}

	@Bean
	public BaseDao baseDao(){return new BaseDaoImpl();}
}
