package letscode.sarafan.repo;

import letscode.sarafan.domain.User;
import letscode.sarafan.domain.UserSubscription;
import letscode.sarafan.domain.UserSubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepo extends JpaRepository<UserSubscription, UserSubscriptionId> {
    List<UserSubscription> findAllBySubscriber(User subscriber);
    List<UserSubscription> findAllByChannel(User channel);

    UserSubscription findByChannelAndSubscriber(User channel, User subscriber);

    List<UserSubscription> findAllBySubscriberAndChannel_NameContainsIgnoreCase(User subscriber, String filter);
    List<UserSubscription> findAllByChannelAndSubscriber_NameContainsIgnoreCase(User channel, String filter);

}
