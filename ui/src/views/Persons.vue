<template>
    <v-row>
    <v-col cols="12" md="10" sm="8" offset-sm="2">
      <v-data-table
        :headers="headers"
        :items="persons"
        sort-by="lastname"
        class="elevation-1"
      >
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>Persons</v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical
            ></v-divider>
            <v-spacer></v-spacer>

            <v-dialog v-model="dialog" max-width="1200px">
              <template v-slot:activator="{ on }">
                <v-btn color="primary" dark class="mb-2" v-on="on">New Person</v-btn>
              </template>
              <v-card>
                <v-card-title>
                  <span class="headline">{{ formTitle }}</span>
                </v-card-title>

                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="editedItem.firstname" label="First Name"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="editedItem.lastname" label="Last Name"></v-text-field>
                      </v-col>
                       <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="editedItem.email" label="Email"></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="editedItem.company" label="Company"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="editedItem.organization" label="Organization"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="editedItem.role" label="Role"></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12" sm="6" md="8">
                       <v-list>
                          <v-list-item-group
                            v-model="editedItem"
                            active-class="border"
                          >
                          <v-list-item
                            v-for="(context, i) in editedItem.contexts"
                            :key="i"
                          >
                          <v-list-item-content>
                            <v-textarea filled>context</v-textarea>
                          </v-list-item-content>
                          </v-list-item>
                         </v-list-item-group>
                      </v-list>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                  <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>

          </v-toolbar>
        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="editItem(item)"
          >
            mdi-pencil
          </v-icon>
          <v-icon
            small
            @click="deleteItem(item)"
          >
            mdi-delete
          </v-icon>
        </template>
        <template v-slot:no-data>
          <v-btn color="primary" @click="initialize">Reset</v-btn>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'

export default {
  data: () => ({
    dialog: false,
    persons: [],
    headers: [
      {
        text: 'Name',
        align: 'start',
        sortable: false,
        value: 'lastname'
      },
      { text: 'First Name', value: 'firstname' },
      { text: 'Email', value: 'email' },
      { text: 'Role', value: 'role' },
      { text: 'Actions', value: 'actions', sortable: false }
    ],
    editedIndex: -1,
    editedItem: {
      lastname: '',
      firstname: '',
      email: '',
      company: '',
      organization: '',
      role: '',
      skills: [],
      needs: [],
      contexts: []
    },
    defaultItem: {
      lastname: '',
      firstname: '',
      email: '',
       company: '',
      organization: '',
      role: '',
      skills: [],
      needs: [],
      contexts: []
    }
  }),
  watch: {
    dialog (val) {
      val || this.close()
    }
  },
  created () {
    this.initialize()
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'New Person' : 'Edit Person'
    }
  },
  methods: {
    initialize () {
      axios.get("/api/v1/persons").then((resp) => (this.persons = resp.data));
    },
    editItem (item) {
      this.editedIndex = this.persons.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    deleteItem (item) {
      const index = this.persons.indexOf(item)
      confirm('Are you sure you want to delete this item?') && this.persons.splice(index, 1)
    },
    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },
    save () {
      if (this.editedIndex > -1) {
        Object.assign(this.persons[this.editedIndex], this.editedItem)
      } else {
        this.persons.push(this.editedItem)
      }
      this.close()
    }
  }
}
</script>