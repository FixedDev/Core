package us.sparknetwork.api.user;

import java.util.UUID;

public interface IUserManager {

    public IParticipator getParticipator(UUID user);

    public IUser getUser(UUID user);

    public IUser loadUser(UUID user);

    public void saveUser(IUser user);
}
