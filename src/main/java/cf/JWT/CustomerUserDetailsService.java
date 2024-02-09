package cf.JWT;

import cf.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@NoArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        cf.POJO.User userDetail = userDao.findByEmail(email);
        if (!Objects.isNull(userDetail)) {
            return new UserDetailsImpl(userDetail);
        } else throw new UsernameNotFoundException("User Not Found");
    }

}
