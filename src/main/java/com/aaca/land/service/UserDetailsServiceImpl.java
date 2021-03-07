package com.aaca.land.service;


import com.aaca.land.entity.User;
import com.aaca.land.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
//
//    @Override
////    @Transactional(readOnly = true)
//    public UserDetailsService getUserByUserName(String userName) throws UsernameNotFoundException {
//        User user = userRepository.findByUserName(userName);
//        if(user==null)
//            throw new UsernameNotFoundException("User 404");
//        return UserPrincipal(user);
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
//        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
//        return (UserDetailsService) new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),grantedAuthorities);
////
//    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        
        Optional<User> user = userRepository.findByUserName(userName);
        return user.map(UserGroupPrincipal::new)
                .orElseThrow(()->new UsernameNotFoundException(userName + "doesn't exist in the database"));



    }
}
