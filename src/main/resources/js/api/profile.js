import Vue from 'vue'

const profile = Vue.resource('/profile{/id}')

export default {
    get: id => profile.get({id}),
    changeSubscription: channelId => Vue.http.post(`/profile/change-subscription/${channelId}`),
    subscriberList: (channelId, filter) => Vue.http.get(`/profile/subscribers/${channelId}`, {params: {filter}}),
    changeSubscriptionStatus: subscriberId => Vue.http.post(`/profile/change-subscription-status/${subscriberId}`),
    userList: filter => Vue.http.get(`/profile/list`, {params: {filter}}),
    subscriptionList: filter => Vue.http.get(`/profile/subscriptions`, {params: {filter}})
}