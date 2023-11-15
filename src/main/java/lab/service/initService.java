package lab.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class initService {

    @Autowired
    private sqlService sqlService;

    @PostConstruct
    public void init(){
        /**
         * redis格式设计:{table2:{table1:{"table2.table1id = table1.id and table2.table1docno = table1.docno"}}}
         */
        sqlService.redisOutkey();
    }
}
