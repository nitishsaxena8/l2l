
Container (v1)
====
Container component written in HTL.

## Features

* Configurable layout type.
* Configurable background image and background color:
    * Background images and colors can be enabled through policy configuration.
    * Color swatches for background color can be defined through policy configuration.
    * Background color can be restricted to only allow swatches through policy configuration.
* Configurable HTML ID attribute.
* Allowed components can be configured through policy configuration.
* Style System support.

### Use Object
The Container component uses the `com.xperience.aem.xpbootstrap.core.component.models.LayoutContainer` Sling model as its Use-object.

### Component Policy Configuration Properties
The following configuration properties are used:

1. `./layout` - defines the layout type, either `simple` (default) or `responsiveGrid`
2. `./layoutDisabled` - if set to true, it is not allowed to change the layout in the edit dialog
3. `./backgroundImageEnabled` - defines whether to display a background image option.
4. `./backgroundColorEnabled` - defines whether to display a background color option.
5. `./backgroundColorSwatchesOnly` -  defines whether or not to display swatches in the background color picker.
6. `./allowedColorSwatches` - defines a list of background color swatches that are allowed to be selected by an author.

It is also possible to define the allowed components for the Container.

### Edit Dialog Properties
The following properties are written to JCR for this Container component and are expected to be available as `Resource` properties:

#### Container Properties
1. `./layout` - defines the layout type, either `simple` (default) or `responsiveGrid`; if no value is defined, the component will fallback to the value defined by the component's policy

#### Common Properties
1. `./backgroundImageReference` - defines the container background image.
2. `./backgroundColor` - defines the container background color.
3. `./id` - defines the component HTML ID attribute.

#### Accessibility
1. `./accessibilityLabel` - defines an accessibility label for the container.
2. `./roleAttribute` - defines a role attribute for the container.

## BEM Description
```
BLOCK cmp-container
```

### Enabling Container Editing Functionality
The following property is required in the proxy component to enable full editing functionality for the Container:

1. `./cq:isContainer` - set to `{Boolean}true`, marks the Container as a container component

## Information
* **Vendor**: Deloitte
* **Version**: v1
* **Compatibility**: AEM 6.5
* **Status**: production-ready
* **Documentation**: [http://localhost:4502/content/adlc-component-library/us/en/home/container-organism.html](http://localhost:4502/content/adlc-component-library/us/en/home/container-organism.html)
* **Component Library**: [http://localhost:4502/content/adlc-component-library/us/en/home/container-organism.html](http://localhost:4502/content/adlc-component-library/us/en/home/container-organism.html)
* **Author**: [Deloitte]()
* **Co-authors**: [Deloitte](), [BhushanBashire](https://github.com/BhushanBashire)

_If you were involved in the authoring of this component and are not credited above, please reach out to us on [GitHub](https://github.com/Deloitte/xperience-reactor)._
