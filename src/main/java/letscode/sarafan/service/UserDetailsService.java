package letscode.sarafan.service;

import letscode.sarafan.domain.User;
import letscode.sarafan.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService {
    private final UserDetailsRepo userDetailsRepo;

    @Autowired
    public UserDetailsService(UserDetailsRepo userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
    }

    public Optional<User> findById(String id) {
        return userDetailsRepo.findById(id);
    }
}
