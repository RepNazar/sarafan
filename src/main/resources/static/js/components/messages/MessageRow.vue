<template>
  <v-card class="my-2">
    <v-card-text primary-title>
      <user-link :user="message.author" size = "48" />
      <div class="pt-3">
        {{ message.text }}
      </div>
    </v-card-text>
    <v-card-actions>
      <v-btn small text rounded @click="edit">Edit</v-btn>
      <v-btn icon @click="del">
        <v-icon>delete</v-icon>
      </v-btn>
    </v-card-actions>
    <comment-list
        :comments="message.comments"
        :message-id="message.id"
    ></comment-list>
  </v-card>
</template>

<script>
import {mapActions} from 'vuex'
import CommentList from 'components/comment/CommentList.vue'
import UserLink from 'components/UserLink.vue'

export default {
  name: 'MessageRow',
  props: ['message', 'editMessage'],
  components: {UserLink, CommentList},
  methods: {
    ...mapActions(['removeMessageAction']),
    edit() {
      this.editMessage(this.message)
    },
    del() {
      this.removeMessageAction(this.message)
    }
  }
}
</script>

<style scoped>

</style>