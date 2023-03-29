export let extensions: Record<string, any> = {
  'Basic Constraints': {
    name: 'Basic Constraints',
    options: [
      {
        name: 'Subject is CA',
        type: 'checkbox',
        value: true,
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
        value: true,
      },
      {
        name: 'Non-repudiation',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Key encipherment',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Data encipherment',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Key agreement',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Encipher only',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'CRL sign',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Decipher only',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Certificate signing',
        type: 'checkbox',
        value: true,
      },
    ],
  },
  'Extended Key Usage': {
    name: 'Extended Key Usage',
    options: [
      {
        name: 'Server authentication',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Client authentication',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Code signing',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Email protection',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Time stamping',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'OCSP signing',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Adobe PDF signing',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'TLS Web Client authentication',
        type: 'checkbox',
        value: true,
      },
      {
        name: 'Any Extended Key Usage',
        type: 'checkbox',
        value: true,
      },
    ],
  },
};

export let templates: Record<string, any> = {
  CA: ['Basic Constraints', 'Key Usage'],
  'Code Signing': ['Key Usage', 'Extended Key Usage'],
};
