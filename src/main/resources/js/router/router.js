import Vue from 'vue'
import VueRouter from 'vue-router'
import MessagesList from 'pages/MessageList.vue'
import Auth from 'pages/Auth.vue'
import Profile from 'pages/Profile.vue'
import SubscriberList from 'pages/SubscriberList.vue'
import SubscriptionList from 'pages/SubscriptionList.vue'
import UserList from 'pages/UserList.vue'

Vue.use(VueRouter)

const routes = [
    {path: '/', component: MessagesList},
    {path: '/auth', component: Auth},
    {path: '/user/:id?', component: Profile},
    {path: '/subscriptions/:id', component: SubscriberList},
    {path: '/subscriptions', component: SubscriptionList},
    {path: '/explore', component: UserList},
    {path: '*', component: MessagesList}
]

export default new VueRouter({
    mode: 'history',
    routes
})