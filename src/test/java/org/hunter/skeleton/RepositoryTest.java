package org.hunter.skeleton;

import org.hunter.pocket.criteria.Restrictions;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.session.SessionFactory;
import org.hunter.skeleton.spine.model.Department;
import org.hunter.skeleton.spine.model.User;
import org.hunter.skeleton.spine.model.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author wujianchuan 2019/1/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Before
    public void setup() {
    }

    @After
    public void destroy() {
    }

    @Test
    public void test5() {
        Session session = SessionFactory.getSession("skeleton");
        session.open();
        this.userRepository.injectSession(session);
        List<Department> departments = session.createCriteria(Department.class).add(Restrictions.equ("enable", true)).list();
        departments.forEach(department -> System.out.println(department.getName()));
        session.close();
    }
}
