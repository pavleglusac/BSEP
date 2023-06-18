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
      },
      spacing: {
        "76": "19rem",
        "82": "20.5rem",
        "106": "26.5rem",
        "120": "30rem",
        "140": "35rem",
      }
    },
  },
  plugins: [],
}