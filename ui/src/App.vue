<template>
  <v-app>
    <v-app-bar
      app
      color="blue"
      dark
    >
     <v-app-bar-nav-icon></v-app-bar-nav-icon>
      <v-toolbar-title>Persons Manager</v-toolbar-title>
      <v-spacer></v-spacer>
       <v-btn to="/" text>
          <v-icon left>mdi-home</v-icon>
          Home
        </v-btn>
         <v-btn to="/persons" text>
          <v-icon left>mdi-person</v-icon>
          Persons
        </v-btn>
         <v-btn to="/meetings" text>
          <v-icon left>mdi-groups</v-icon>
          Meeting
        </v-btn>
    </v-app-bar>

     <v-content>
      <v-container>
        <router-view />
      </v-container>
    </v-content> 
    
  </v-app>
</template>

<script>
export default {
  name: 'App',

  data: () => ({
    //
  }),
};
</script>
