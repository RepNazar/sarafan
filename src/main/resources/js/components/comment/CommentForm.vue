<template>
  <v-row align="center" class="px-3">
    <v-text-field
        label="Add comment"
        placeholder="Write something"
        v-model="text"
        @keyup.enter="save"
    />
    <v-btn text @click="save" class="mt-1">
      Add
    </v-btn>
  </v-row>
</template>

<script>
import {mapActions} from 'vuex'

export default {
  name: 'CommentForm',
  props: ['messageId'],
  data() {
    return {
      text: ''
    }
  },
  methods: {
    ...mapActions(['addCommentAction']),
    async save() {
      await this.addCommentAction({
        text: this.text,
        message: {
          id: this.messageId
        }
      })
      this.text = ''
    }
  }
}
</script>

<style scoped>

</style>