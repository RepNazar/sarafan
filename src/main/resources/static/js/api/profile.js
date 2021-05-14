import Vue from 'vue'

const profile = Vue.resource('/profile{/id}')

export default {
    get: id => profile.get({id}),
    changeSubscription: channelId => Vue.http.post(`/profile/change-subscription/${channelId}`),
    subscriberList: channelId => Vue.http.get(`/profile/subscribers/${channelId}`),
    changeSubscriptionStatus: subscriberId => Vue.http.post(`/profile/change-subscription-status/${subscriberId}`)
}