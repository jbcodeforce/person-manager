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
            <v-divider class="mx-4" inset vertical></v-divider>
            <v-spacer></v-spacer>

            <v-dialog v-model="dialog" max-width="1200px">
              <template v-slot:activator="{ on }">
                <v-btn color="primary" dark class="mb-2" v-on="on"
                  >New Person</v-btn
                >
              </template>
              <v-card>
                <v-card-title>
                  <span class="headline">{{ formTitle }}</span>
                </v-card-title>
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.firstname"
                          label="First Name"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.lastname"
                          label="Last Name"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.email"
                          label="Email"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.company"
                          label="Company"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.organization"
                          label="Organization"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.role"
                          label="Role"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                       <v-row>
                      <v-col cols="12">
                        <p>Skills</p>
                        <card-list v-model="editedItem.skills" #default="{ item }">
                          <v-row>
                            <v-col cols="12">
                              <v-text-field v-model="item.text" label="Skill" hide-details />
                            </v-col>
                          </v-row>
                        </card-list>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="auto" class="py-0 mx-auto">
                        <v-btn @click="addSkill" text>
                          <v-icon> mdi-plus </v-icon>
                          Add
                        </v-btn>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12">
                        <p>Contexts</p>
                        <card-list v-model="editedItem.contexts" #default="{ item }">
                          <v-row>
                            <v-col cols="12">
                              <v-text-field v-model="item.text" label="Context" hide-details />
                            </v-col>
                          </v-row>
                        </card-list>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="auto" class="py-0 mx-auto">
                        <v-btn @click="addContext" text>
                          <v-icon> mdi-plus </v-icon>
                          Add
                        </v-btn>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12">
                        <p>Needs</p>
                        <card-list v-model="editedItem.needs" #default="{ item }">
                          <v-row>
                            <v-col cols="12">
                              <v-text-field v-model="item.text" label="Need" hide-details />
                            </v-col>
                          </v-row>
                        </card-list>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="auto" class="py-0 mx-auto">
                        <v-btn @click="addNeed" text>
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
        <template v-slot:no-data>
          <v-btn color="primary" @click="initialize">Reset</v-btn>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>

<script>
import axios from "axios";
import CardList from "./CardList";

export default {
  components: {
    CardList,
  },
  data: () => ({
    dialog: false,
    persons: [],
    needs: [
      {
        id: 0,
        text: "First need",
      },
      {
        id: 1,
        text: "second need",
      },
    ],
    needCounter: 10,
    skillCounter: 10,
    ctxCounter: 10,
    headers: [
      {
        text: "Name",
        align: "start",
        sortable: false,
        value: "lastname",
      },
      { text: "First Name", value: "firstname" },
      { text: "Email", value: "email" },
      { text: "Role", value: "role" },
      { text: "Actions", value: "actions", sortable: false },
    ],
    editedIndex: -1,
    editedItem: {
      lastname: "",
      firstname: "",
      email: "",
      company: "",
      organization: "",
      role: "",
      skills: [],
      needs: [],
      contexts: [],
    },
    defaultItem: {
      lastname: "",
      firstname: "",
      email: "",
      company: "",
      organization: "",
      role: "",
      skills: [],
      needs: [],
      contexts: [],
    },
    newContext: "",
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
      return this.editedIndex === -1 ? "New Person" : "Edit Person";
    },
  },
  methods: {
    initialize() {
      axios.get("/api/v1/persons").then((resp) => (this.persons = resp.data));
      console.log(this.persons);
    },
    addNeed() {
      this.editedItem.needs.push({ id: this.needCounter++,text:"new need" });
    },
    addContext(){
      this.editedItem.contexts.push({ id: this.ctxCounter++,text:"new context" });
    },
    addSkill(){
      this.editedItem.skills.push({ id: this.skillCounter++,text:"new skill" });
    },
    editItem(item) {
      this.editedIndex = this.persons.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.skillCounter = item.skills.length;
      this.ctxCounter = item.contexts.length;
      this.needCounter = item.needs.length;
      this.dialog = true;
    },
    deleteItem(item) {
      const index = this.persons.indexOf(item);
      confirm("Are you sure you want to delete this item?") &&
        this.persons.splice(index, 1);
    },
    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },
    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.persons[this.editedIndex], this.editedItem);
      } else {
        this.persons.push(this.editedItem);
      }
      this.close();
    },
  },
};
</script>