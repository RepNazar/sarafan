<template>
  <v-container>
    <v-layout column align-center>
      <v-row align="center">
        <v-text-field
            label="Search"
            placeholder="Write something"
            v-model="text"
            @keyup.enter="filter"
            size="24"
        />
        <v-btn icon @click="filter" class="mt-1">
          <v-icon>search</v-icon>
        </v-btn>
      </v-row>

      <v-list>
        <v-list-item v-for="item in subscriptions">
          <user-link :user="item.subscriber" size="24"></user-link>
          <v-btn @click="changeSubscriptionStatus(item.subscriber.id)">
            {{ item.active ? "Dismiss" : "Approve" }}
          </v-btn>
        </v-list-item>
      </v-list>
    </v-layout>
  </v-container>
</template>

<script>
import profileApi from 'api/profile'
import UserLink from 'components/UserLink.vue'

export default {
  name: 'Subscriptions',
  components: {UserLink},
  data() {
    return {
      subscriptions: [],
      text: ''
    }
  },
  methods: {
    async changeSubscriptionStatus(subscriberId) {
      await profileApi.changeSubscriptionStatus(subscriberId)

      const subscriptionIndex = this.subscriptions.findIndex(item =>
          item.subscriber.id === subscriberId)
      const subscription = this.subscriptions[subscriptionIndex]

      this.subscriptions = [
        ...this.subscriptions.slice(0, subscriptionIndex),
        {
          ...subscription,
          active: !subscription.active
        },
        ...this.subscriptions.slice(subscriptionIndex + 1)
      ]
    },
    async filter() {
      const resp = await profileApi.subscriberList(this.$store.state.profile.id, this.text)
      this.subscriptions = await resp.json()
    }
},
  async beforeMount() {
    await this.filter()
  }
}
</script>

<style scoped>

</style>