package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Iterator;

/**
 * EUserSaveRepositoryImpl
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 12, 2018
 */
@RequiredArgsConstructor
@Transactional
public class EUserRepositoryImpl implements EUserRepositoryCustom {

    private final BCryptPasswordEncoder encoder;

    private final EntityManager entityManager;

    @Override
    public <S extends EUser> S save(S s) {
        persist(s);
        return s;
    }

    @Override
    public <S extends EUser> Iterable<S> save(Iterable<S> it) {
        final Iterator<S> iterator = it.iterator();
        S s;
        while (iterator.hasNext()) {
            s = it.iterator().next();
            persist(s);
        }
        return it;
    }

    private void persist(EUser u) {
        if (u.getPassword() != null) {
            u.setPassword(encoder.encode(u.getPassword()));
        }
        entityManager.persist(u);
    }
}
