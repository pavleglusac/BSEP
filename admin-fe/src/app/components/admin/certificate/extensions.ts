export let extensions: Record<string, any> = {
  'Basic Constraints': {
    name: 'Basic Constraints',
    options: [
      {
        name: 'Subject is CA',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Path length',
        type: 'number',
        value: 0,
      },
    ],
  },
  'Key Usage': {
    name: 'Key Usage',
    options: [
      {
        name: 'Digital signature',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Non-repudiation',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Key encipherment',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Data encipherment',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Key agreement',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Encipher only',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'CRL sign',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Decipher only',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Certificate signing',
        type: 'checkbox',
        value: false,
      },
    ],
  },
  'Extended Key Usage': {
    name: 'Extended Key Usage',
    options: [
      {
        name: 'Server authentication',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Client authentication',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Code signing',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Email protection',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Time stamping',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'OCSP signing',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Adobe PDF signing',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'TLS Web Client authentication',
        type: 'checkbox',
        value: false,
      },
      {
        name: 'Any Extended Key Usage',
        type: 'checkbox',
        value: false,
      },
    ],
  },
};

export let templates: Record<string, any> = {
  CA: ['Basic Constraints', 'Key Usage'],
  'Code Signing': ['Key Usage', 'Extended Key Usage'],
};
