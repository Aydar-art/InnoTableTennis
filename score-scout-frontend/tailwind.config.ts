import { fontFamily } from 'tailwindcss/defaultTheme';

/** @type {import('tailwindcss').Config} */
export default {
  darkMode: ['class'],
  content: ['./src/**/*.{html,js,svelte,ts}'],
  theme: {
    extend: {
      colors: {
        primary: '#D22D19',
        'primary-hover': '#A82313',
        'secondary-btn-hover': '#FCEAE8',
        secondary: '#F6DADA',
        'foreground-hint': '#A1A1A1',
        'foreground-primary': '#222222',
        'icon-muted': '#535353',
        'foreground-select-option': '#654E4F',
        'toggle-border': '#C6C7C1',
        'text-hint': '#ACA4A2',
        subtle: 'rgba(0, 0, 0, 0.04)',
        'nav-inactive': '#E8968C',
      },
      borderRadius: {
        '8': '8px',
        '5': '5px',
        '4': '4px',
        '3': '3px',
        '2': '2px',
        '20': '20px',
        '30': '30px',
      },
      fontFamily: {
        sans: [...fontFamily.sans],
      },
      spacing: {
        '15px': '15px',
      },
      scale: {
        101: '1.01',
        102: '1.02',
        103: '1.03',
        104: '1.04',
      },
      boxShadow: {
        form: '4px 4px 100px 8px #FF6D4C40',
      },
    },
  },
};
