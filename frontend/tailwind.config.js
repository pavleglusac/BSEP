/** @type {import('tailwindcss').Config} */
module.exports = {
  mode: "jit",
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        "dark": "#252527",
        "medium": "#92898A",
        "main": "#18A999",
        "light": "#FDFFFC",
      }
    },
  },
  plugins: [],
}