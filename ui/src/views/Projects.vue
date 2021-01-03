<template>
  <v-row>
    <v-col cols="12" md="10" sm="8" offset-sm="2">
      <v-data-table
        :headers="headers"
        :items="projects"
        sort-by="date"
        class="elevation-2"
      >
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>Projects</v-toolbar-title>
            <v-divider class="mx-4" inset vertical></v-divider>
            <v-spacer></v-spacer>
            <v-dialog v-model="dialog" max-width="1200px">
              <template v-slot:activator="{ on }">
                <v-btn color="primary" dark class="mb-2" v-on="on"
                  >New Project</v-btn
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
                          v-model="editedItem.customerName"
                          label="Customer"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="6">
                        <v-select
                          v-model="editedItem.type"
                          :items="typeList"
                          label="Project Type"
                          persistent-hint
                          return-object
                          single-line
                          required
                        ></v-select>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-text-field
                        v-model="editedItem.name"
                        label="Project Name"
                        required
                      ></v-text-field>
                    </v-row>

                    <v-row>
                      <v-col cols="12">
                        <h3>Notes</h3>
                        <card-list
                          v-model="editedItem.notes"
                          #default="{ item }"
                        >
                          <v-row>
                            <v-col cols="12">
                              <vue-editor v-model="item.text"></vue-editor>
                            </v-col>
                          </v-row>
                        </card-list>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="auto" class="py-0 mx-auto">
                        <v-btn @click="addNote" text>
                          <v-icon> mdi-plus </v-icon>
                          Add
                        </v-btn>
                      </v-col>
                    </v-row>

                    <v-row>
                      <v-col cols="12">
                        <h3>To Do</h3>
                        <card-list
                          v-model="editedItem.todos"
                          #default="{ item }"
                        >
                          <v-row>
                            <v-col cols="12">
                              <vue-editor v-model="item.text"></vue-editor>
                            </v-col>
                          </v-row>
                        </card-list>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="auto" class="py-0 mx-auto">
                        <v-btn @click="addTodo" text>
                          <v-icon> mdi-plus </v-icon>
                          Add
                        </v-btn>
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
import { VueEditor } from "vue2-editor";
import axios from "axios";
import CardList from "./CardList";

let backendURL = "http://localhost:8080/projects";

export default {
  components: {
    VueEditor,
    CardList
  },
  data: () => ({
    projects: [],
    typeList: ["Customer", "Internal"],
    noteCounter: 0,
    todoCounter: 0,
    dialog: false,
    editedIndex: -1,
    editedItem: {
      id: 0,
      type: "",
      name: "",
      status: "",
      customerName: "",
      creationDate: "",
      updateDate: "",
      notes: [],
      todos: [],
    },
    defaultItem: {
      id: 0,
      type: "",
      name: "",
      status: "",
      customerName: "",
      notes: [],
      todos: [],
    },
    headers: [
      {
        text: "Name",
        align: "start",
        sortable: true,
        value: "name",
      },
      {
        text: "Customer",
        align: "start",
        sortable: true,
        value: "customerName",
      },
      {
        text: "Creation Date",
        align: "start",
        value: "creationDate",
      },
      {
        text: "Updated Date",
        align: "start",
        value: "updateDate",
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
      return this.editedIndex === -1 ? "New Project" : "Edit Project";
    },
  },
  methods: {
    initialize() {
      axios.get(backendURL).then((resp) => (this.projects = resp.data));
    },
    editItem(item) {
      this.editedIndex = this.projects.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item) {
      const index = this.projects.indexOf(item);
      confirm("Are you sure you want to delete this item?") &&
        this.meetings.splice(index, 1);
      axios.delete(backendURL, item);
    },
    addNote() {
      this.editedItem.notes.push({ id: this.noteCounter++, text: "new note" });
    },
    addTodo() {
      this.editedItem.todos.push({ id: this.todoCounter++, text: "new to do" });
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
        axios
          .put(backendURL, this.editedItem)
          .then((resp) => {
            console.log(resp);
            this.editedItem = resp.data;
          })
          .catch(function (error) {
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
        Object.assign(this.projects[this.editedIndex], this.editedItem);
      } else {
        this.projects.push(this.editedItem);
      }
      this.close();
    },
  },
};
</script>