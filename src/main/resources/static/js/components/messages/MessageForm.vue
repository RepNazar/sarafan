<template>
  <v-row align="center">
    <v-text-field
        label="New message"
        placeholder="Write something"
        v-model="text"
    />
    <v-btn text @click="save" class="mt-1">
      Save
    </v-btn>
  </v-row>
</template>

<script>
import MessagesApi from 'api/messages'

export default {
  props: ['messages', 'messageAttr'],
  data() {
    return {
      text: '',
      id: ''
    }
  },
  watch: {
    messageAttr(newVal, oldVal) {
      this.text = newVal.text
      this.id = newVal.id
    }
  },
  methods: {
    save() {
      const message = {
        id: this.id,
        text: this.text
      }

      if (this.id) {
        MessagesApi.update(message).then(result =>
            result.json().then(data => {
              const index = this.messages.findIndex(item => item.id === data.id)
              this.messages.splice(index, 1, data)
            })
        )
      } else {
        MessagesApi.add(message).then(result =>
            result.json().then(data => {
              const index = this.messages.findIndex(item => item.id === data.id)

              if (index > -1) {
                this.messages.splice(index, 1, data)
              } else {
                this.messages.push(data)
              }
            })
        )
      }
      this.text = ''
      this.id = ''
    }
  }
}
</script>

<style>

</style>