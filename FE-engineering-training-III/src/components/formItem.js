const FormItem = {
  props: {
    renderData: {
      required: true
    }
  },
  render (h) {
    const { schema, form } = this.$props.renderData
    if (typeof schema.render === 'function') {
      return schema.render(h)
    } else if (schema.type !== 'text') {
      const children = typeof schema.getChildren === 'function' ? schema.getChildren(h, schema, form) : null
      return h(`el-${schema.type}`, {
        style: 'width: 100%',
        class: schema.class || '',
        props: {
          value: form[schema.field],
          ...schema.props || {}
        },
        attrs: {
          ...schema.attrs || {}
        },
        on: {
          input: (value) => {
            this.$emit('input', schema, value)
          },
          ...schema.on || {}
        },
        nativeOn: {
          ...schema.nativeOn || {}
        }
      }, children)
    } else {
      return h('span', form[schema.field])
    }
  }
}

export default FormItem

export const getSelectOptions = (options = [], h) => {
  return Array.isArray(options) ? options.map(option => {
    return h('el-option', {
      props: {
        label: option.label,
        value: option.value
      }
    })
  }) : null
}
