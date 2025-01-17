package letscode.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import letscode.sarafan.domain.User;
import letscode.sarafan.domain.UserSubscription;
import letscode.sarafan.domain.Views;
import letscode.sarafan.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("{id}")
    @JsonView(Views.FullProfile.class)
    public User get(@PathVariable("id") User user) {
        return user;
    }

    @GetMapping("list")
    @JsonView(Views.IdName.class)
    public List<User> list(@RequestParam(name = "filter", required = false) String filter) {
        if (filter == null || filter.isEmpty()) {
            return profileService.getAllUsers();
        } else {
            return profileService.getAllUsersByNameContains(filter);
        }
    }

    @PostMapping("change-subscription/{channelId}")
    @JsonView(Views.FullProfile.class)
    public User changeSubscription(
            @AuthenticationPrincipal User subscriber,
            @PathVariable("channelId") User channel
    ) {
        if (subscriber.equals(channel)) {
            return channel;
        } else {
            return profileService.changeSubscription(channel, subscriber);
        }
    }

    @GetMapping("subscribers/{channelId}")
    @JsonView(Views.IdName.class)
    public List<UserSubscription> subscribers(
            @PathVariable("channelId") User channel,
            @RequestParam(name = "filter", required = false) String filter
    ) {
        if (filter == null || filter.isEmpty()) {
            return profileService.getSubscribers(channel);
        } else {
            return profileService.getSubscribersByNameContains(channel, filter);
        }

    }

    @GetMapping("subscriptions")
    @JsonView(Views.IdName.class)
    public List<UserSubscription> subscriptions(
            @AuthenticationPrincipal User channel,
            @RequestParam(name = "filter", required = false) String filter
    ) {
        if (filter == null || filter.isEmpty()) {
            return profileService.getSubscriptions(channel);
        } else {
            return profileService.getSubscriptionsByNameContains(channel, filter);
        }

    }

    @PostMapping("change-subscription-status/{subscriberId}")
    @JsonView(Views.IdName.class)
    public UserSubscription changeSubscriptionStatus(
            @AuthenticationPrincipal User channel,
            @PathVariable("subscriberId") User subscriber
    ) {
        return profileService.changeSubscriptionStatus(channel, subscriber);
    }
}
