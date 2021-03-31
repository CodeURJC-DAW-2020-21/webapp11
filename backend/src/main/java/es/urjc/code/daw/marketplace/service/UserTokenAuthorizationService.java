package es.urjc.code.daw.marketplace.service;

public interface UserTokenAuthorizationService {

    boolean requesterCanManipulateUser(Long userId);

    boolean requesterIsOperator();

}
