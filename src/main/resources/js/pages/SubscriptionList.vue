<template>
  <v-container>
    <v-layout column align-center>
      <v-list>
        <v-list-tile>
          <v-text-field
              label="Search"
              placeholder="Write something"
              v-model="text"
              @keyup.enter="filter"
          />
          <v-btn icon @click="filter" class="mt-1">
            <v-icon>search</v-icon>
          </v-btn>
        </v-list-tile>

        <v-list-tile v-for="item in subscriptions" :key="item.id">
          <user-link :user="item.channel"></user-link>
        </v-list-tile>
      </v-list>
    </v-layout>
  </v-container>
</template>

<script>
import profileApi from 'api/profile'
import UserLink from 'components/UserLink.vue'

export default {
  name: 'Subscribers',
  components: {UserLink},
  data() {
    return {
      subscriptions: [],
      text: ''
    }
  },
  async beforeMount() {
    await this.filter();
  },
  methods: {
    async filter() {
      const resp = await profileApi.subscriptionList(this.text)
      this.subscriptions = await resp.json()
    }
  }
}
</script>

<style scoped>

</style>