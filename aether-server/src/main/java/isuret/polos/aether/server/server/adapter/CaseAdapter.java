package isuret.polos.aether.server.server.adapter;

import isuret.polos.aether.database.Database;
import isuret.polos.aether.domains.Case;
import isuret.polos.aether.domains.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("case/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CaseAdapter {

    private Database database;

    @PostConstruct
    private void init() {
        database = new Database(new File("database"));
    }

    @GetMapping()
    public List<Case> getAllCaseNames(@RequestHeader("userName") String userName, @RequestHeader("password") String password) {

        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);

        return database.getAllCases(user);
    }
}
