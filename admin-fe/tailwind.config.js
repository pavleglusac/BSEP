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
        "accent": "#18A999",
        "accent-dark": "#117D71",
        "light": "#FDFFFC",
      }
    },
  },
  plugins: [],
}