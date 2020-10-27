<template>
  <v-row>
    <v-col cols="12" md="10" sm="8" offset-sm="2">
      <v-data-table
        :headers="headers"
        :items="meetings"
        sort-by="date"
        class="elevation-2"
      >
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>Meetings</v-toolbar-title>
            <v-divider class="mx-4" inset vertical></v-divider>
            <v-spacer></v-spacer>
            <v-dialog v-model="dialog" max-width="1200px">
              <template v-slot:activator="{ on }">
                <v-btn color="primary" dark class="mb-2" v-on="on"
                  >New Meeting</v-btn
                >
              </template>
              <v-card>
                <v-card-title>
                  <span class="headline">{{ formTitle }}</span>
                </v-card-title>

                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="10" sm="6" md="6">
                        <v-text-field
                          v-model="editedItem.customer"
                          label="Customer"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="6">
                        <v-text-field
                          v-model="editedItem.title"
                          label="Meeting Title"
                          required
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <p>Context</p>
                      <v-col cols="12" sm="8" md="12">
                        <vue-editor v-model="editedItem.context"></vue-editor>
                      </v-col>
                    </v-row>
                    <v-row>
                      <p>Attendees</p>
                      <v-col cols="12" sm="8" md="12">
                        <v-textarea
                          filled
                          required
                          v-model="editedItem.attendees"
                          label="Attendees"
                        ></v-textarea>
                      </v-col>
                    </v-row>
                    <v-row>
                      <p>Notes</p>
                      <v-col cols="12" sm="8" md="12">
                        <vue-editor v-model="editedItem.notes"></vue-editor>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="close"
                    >Cancel</v-btn
                  >
                  <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-toolbar>
        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="editItem(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>

<script>
import { VueEditor } from "vue2-editor"
import axios from "axios";

let backendURL = "/api/v1/meetings";

export default {
  components:{
    VueEditor
  },
  data: () => ({
    meetings: [],
    dialog: false,
    editedIndex: -1,
    editedItem: {
      _id: "",
      _rev: "",
      title: "",
      customer: "",
      context: "",
      notes: "",
      creationDate: "",
      attendees: "",
      todos: [],
      active: true,
    },
    defaultItem: {
      title: "",
      customer: "",
      context: "",
      notes:"",
      attendees: "",
      active: true,
    },
    headers: [
      {
        text: "Active",
        align: "start",
        type: "check",
        value: "active",
      },
      {
        text: "Customer",
        align: "start",
        sortable: true,
        value: "customer",
      },
      {
        text: "Date",
        align: "start",
        sortable: true,
        value: "creationDate",
      },
      {
        text: "Meeting title",
        align: "start",
        sortable: false,
        value: "title",
      },
      { text: "Actions", value: "actions", sortable: false },
    ],
  }),
  watch: {
    dialog(val) {
      val || this.close();
    },
  },
  created() {
    this.initialize();
  },
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Meeting" : "Edit Meeting";
    },
  },
  methods: {
    initialize() {
      axios.get(backendURL).then((resp) => (this.meetings = resp.data));
    },
    editItem(item) {
      this.editedIndex = this.meetings.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item) {
      const index = this.meetings.indexOf(item);
      confirm("Are you sure you want to delete this item?") &&
      this.meetings.splice(index, 1);
      axios.delete(backendURL,item)
    },
    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },
    save() {
       if (this.editedItem._rev !== null) {
        axios.put(backendURL,this.editedItem)
        .then((resp) => {
            console.log(resp);
            this.editedItem = resp.data
          }).catch(function (error) {
          console.log(error);
        });
      } else {
        axios
        .post(backendURL, this.editedItem)
        .then(function (response) {
          console.log(response);
          this.editedItem = response.data;
         
        })
        .catch(function (error) {
          console.log(error);
        });
      }
      if (this.editedIndex > -1) {
          Object.assign(this.meetings[this.editedIndex], this.editedItem);
      } else {
          this.meetings.push(this.editedItem);
      }
      this.close();
    },
  },
};
</script>