/** @type {import('tailwindcss').Config} */
module.exports = {
 content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        primary : {
          violet: "#8189BD",
          gray: {light:"#BABED4", dark:"#969cbd"}
        }, 
        secondary: {
          white: "#FAFAFA",
          gray: "#D4D6E4"
        }
    },
    boxShadow: {
      around: '0px 2px 20px 1px rgb(0 0 0 / 0.1)',
    },
  },
  plugins: [],
  }
}
